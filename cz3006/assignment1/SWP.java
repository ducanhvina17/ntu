/*===============================================================*
*  File: SWP.java                                               *
*                                                               *
*  This class implements the sliding window protocol            *
*  Used by VMach class					         *
*  Uses the following classes: SWE, Packet, PFrame, PEvent,     *
*                                                               *
*  Author: Professor SUN Chengzheng                             *
*          School of Computer Engineering                       *
*          Nanyang Technological University                     *
*          Singapore 639798                                     *
*===============================================================*/
import java.util.Timer;
import java.util.TimerTask;

public class SWP
{
	/*========================================================================*
	the following are provided, do not change them!!
	*========================================================================*/
	//the following are protocol constants.
	public static final int MAX_SEQ = 7;
	public static final int NR_BUFS = (MAX_SEQ + 1)/2;

	// the following are protocol variables
	private int oldest_frame = 0;
	private PEvent event = new PEvent();
	private Packet out_buf[] = new Packet[NR_BUFS];

	//the following are used for simulation purpose only
	private SWE swe = null;
	private String sid = null;

	//Constructor
	public SWP(SWE sw, String s)
	{
		swe = sw;
		sid = s;
	}

	//the following methods are all protocol related
	private void init()
	{
		for (int i = 0; i < NR_BUFS; i++)
		out_buf[i] = new Packet();
	}

	private void wait_for_event(PEvent e)
	{
		swe.wait_for_event(e); //may be blocked
		oldest_frame = e.seq;  //set timeout frame seq
	}

	private void enable_network_layer(int nr_of_bufs)
	{
		//network layer is permitted to send if credit is available
		swe.grant_credit(nr_of_bufs);
	}

	private void from_network_layer(Packet p)
	{
		swe.from_network_layer(p);
	}

	private void to_network_layer(Packet packet)
	{
		swe.to_network_layer(packet);
	}

	private void to_physical_layer(PFrame fm)
	{
		System.out.println("SWP: Sending frame: seq = " + fm.seq + " ack = " + fm.ack + " kind = " +
		PFrame.KIND[fm.kind] + " info = " + fm.info.data );
		System.out.flush();
		swe.to_physical_layer(fm);
	}

	private void from_physical_layer(PFrame fm)
	{
		PFrame fm1 = swe.from_physical_layer();
		fm.kind = fm1.kind;
		fm.seq = fm1.seq;
		fm.ack = fm1.ack;
		fm.info = fm1.info;
	}


	/*===========================================================================*
	implement your Protocol Variables and Methods below:
	*==========================================================================*/
	boolean no_nak = true;
	private Packet in_buf[] = new Packet[NR_BUFS];	// Input buffers
	private boolean arrived[] = new boolean[NR_BUFS];
	private Timer normal_timer[] = new Timer[NR_BUFS];	// Each buffer has its own timer
	private Timer ack_timer; // Only one ack timer needed
	private static final int NORMAL_TIMEOUT = 1500;	// Timer will timeout after 1.5 second
	private static final int ACK_TIMEOUT = 1000;	// Ack timer will timeout after 1 second.

	private void init_var()
	{
		for (int i = 0; i < NR_BUFS; i++)
		{
			arrived[i] = false;
			in_buf[i] = new Packet();
		}
	}

	public void protocol6()
	{
		//oldest_frame = MAX_SEQ + 1;
		int ack_expected = 0;   // Lower edge of sender's window
		int next_frame_to_send = 0; // Upper edge of sender's window + 1
		int frame_expected = 0; // Lower edge of receiver's window
		int too_far = NR_BUFS;    // Upper edge of receiver's window + 1
		// int nbuffered = 0;

		PFrame frame = new PFrame();	// New frame from physical layer

		init();
		init_var();	// Initialize variable
		enable_network_layer(NR_BUFS); // Initialize

		while(true)
		{
			wait_for_event(event);
			switch(event.type)
			{
				case (PEvent.NETWORK_LAYER_READY):  // Accept, save and transmit a new frame
					// nbuffered++;    // Expand the window
					from_network_layer(out_buf[next_frame_to_send % NR_BUFS]);  // Fetch new packet
					send_frame(PFrame.DATA, next_frame_to_send, frame_expected, out_buf);   // Transmit the frame
					next_frame_to_send = inc(next_frame_to_send);    // Advance upper windows edge
					break;
				case (PEvent.FRAME_ARRIVAL):   // A data or control fame has arrived
					from_physical_layer(frame); // Fetch incoming frame from physical layer
					if (frame.kind == PFrame.DATA)	// Undamaged frame has arrived
					{
						if (frame.seq != frame_expected && no_nak)
							send_frame(PFrame.NAK, 0, frame_expected, out_buf);
						else
							start_ack_timer();
						if (between(frame_expected, frame.seq, too_far) && !arrived[frame.seq % NR_BUFS])
						{
							// Frames may be accepted in any order
							arrived[frame.seq % NR_BUFS] = true;	// Mark buffer as full
							in_buf[frame.seq % NR_BUFS] = frame.info;	// Insert data into buffer
							while (arrived[frame_expected % NR_BUFS])
							{
								// Pass frames to advance window
								to_network_layer(in_buf[frame_expected % NR_BUFS]);
								no_nak = true;
								arrived[frame_expected % NR_BUFS] = false;
								frame_expected = inc(frame_expected);	// Advance lower edge of receiver's window
								too_far = inc(too_far);	// Advance upper edge of receiver's window
								start_ack_timer();
							}
						}
					}
					if (frame.kind == PFrame.NAK &&
						between(ack_expected, (frame.ack + 1) % (MAX_SEQ + 1), next_frame_to_send))
						send_frame(PFrame.DATA, (frame.ack + 1) % (MAX_SEQ + 1), frame_expected, out_buf);
					while (between(ack_expected, frame.ack, next_frame_to_send))
					{
						// nbuffered--;	// Handle piggybacked ack
						stop_timer(ack_expected % NR_BUFS);	// Frame arrived intact
						ack_expected = inc(ack_expected);	// Advance lower edge of sender's window
						enable_network_layer(1);	// Suspect losing frame is frame is not in order
					}
					break;
				case (PEvent.CKSUM_ERR):
					if (no_nak)
						send_frame(PFrame.NAK, 0, frame_expected, out_buf);	// Damaged frame
					break;
				case (PEvent.TIMEOUT):	// Timed out
					send_frame(PFrame.DATA, oldest_frame, frame_expected, out_buf);
					break;
				case (PEvent.ACK_TIMEOUT):	// Ack timer expired, resend ack for piggybacking
					send_frame(PFrame.ACK, 0, frame_expected, out_buf);
					break;
				default:
					System.out.println("SWP: undefined event type = " + event.type);
					System.out.flush();
			}
		}
	}

	/* Construct and send a data, ack or nak frame */
	private void send_frame(int frame_kind, int frame_num, int frame_expected, Packet buffer[])
	{
		PFrame frame = new PFrame();   // Scratch variable
		frame.kind = frame_kind;    // kind == data or ack or nak

		if (frame_kind == PFrame.DATA)
			frame.info = buffer[frame_num % NR_BUFS];

		frame.seq = frame_num;   // Only meaningful for data frames
		frame.ack = (frame_expected + MAX_SEQ) % (MAX_SEQ + 1);	// Piggyback ack

		if (frame_kind == PFrame.NAK)
			no_nak = false;    // One nak per frame

		to_physical_layer(frame);   // Transmit the frame

		if (frame_kind == PFrame.DATA)
			start_timer(frame_num);

		stop_ack_timer();   // Piggybacked
	}

	/* Circular increment of a sequence number over the sequence number space */
	private int inc(int seq)
	{
	    return (seq + 1) % (MAX_SEQ + 1);
	}

	/* Same as between in protocol5, but shorter and more obscure. */
	private boolean between(int a, int b, int c)
	{
		return ((a <= b) && (b < c)) || ((c < a) && (a <= b)) || ((b < c) && (c < a));
	}

	/* Start normal timer */
	private void start_timer(int seq)
	{
		stop_timer(seq);
		normal_timer[seq % NR_BUFS] = new Timer();
		normal_timer[seq % NR_BUFS].schedule(new SWP_TimerTask(seq), NORMAL_TIMEOUT);
	}

	/* Stop normal timer */
	private void stop_timer(int seq)
	{
		if (normal_timer[seq % NR_BUFS] != null)
			normal_timer[seq % NR_BUFS].cancel();
	}

	/* Start ack timer */
	private void start_ack_timer( )
	{
		stop_ack_timer();
		ack_timer = new Timer();
		ack_timer.schedule(new SWP_ACK_TimerTask(), ACK_TIMEOUT);
	}

	/* Stop ack timer */
	private void stop_ack_timer()
	{
		if (ack_timer != null)
			ack_timer.cancel();
	}

	/* Normal timer class */
	class SWP_TimerTask extends TimerTask
	{
		int seq;

		private SWP_TimerTask(int seq)
		{
	        this.seq = seq;
	    }

	    @Override
	    public void run()
	    {
			stop_timer(this.seq);
			swe.generate_timeout_event(seq);
	    }
	}

	/* Ack timer class */
	private class SWP_ACK_TimerTask extends TimerTask
	{
		@Override
		public void run()
		{
			stop_ack_timer();
	        swe.generate_acktimeout_event();
		}
	}
}//End of class

/* Note: In class SWE, the following two public methods are available:
. generate_acktimeout_event() and
. generate_timeout_event(seqnr).

To call these two methods (for implementing timers),
the "swe" object should be referred as follows:
swe.generate_acktimeout_event(), or
swe.generate_timeout_event(seqnr).
*/
