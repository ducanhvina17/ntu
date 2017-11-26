import serial
import time


class SerCon(object):
    def __init__(self):
        self.isConnected = False
        self.changeP = False

    def setup(self):
        try:
            if self.changeP:
                self.changeP = False
                self.port = '/dev/ttyACM1'
            else:
                self.changeP = True
                self.port = '/dev/ttyACM0'
            self.b_rate = 115200
            fmessage = '\nSetup SerialCon'
            fmessage = "\nPort: " + str(self.port)
            fmessage = "\nBaud Rate: " + str(self.b_rate)
            print(fmessage)
        except Exception as e:
            fmessage = '\nSerial Setup Error: ' + str(e)
            print(fmessage)

    def connect(self):
        self.setup()
        try:
            self.sr = serial.Serial(self.port, self.b_rate)
            fmessage = '\nSerial Connected'
            print(fmessage)
            self.isConnected = True
        except Exception as e:
            fmessage = '\nSerial Connection Error: ' + str(e)
            print(fmessage)
            self.connect()

    def write(self, sMsg):
        try:
            self.sr.write(sMsg)
            self.sr.flush()
        except Exception as e:
            fmessage = '\nSerial Write Error: ' + str(e)
            print(fmessage)
            self.connect()

    def read(self):
        try:
            rMsg = self.sr.readline()
            return rMsg
        except Exception as e:
            fmessage = '\nSerial Read Error: ' + str(e)
            print(fmessage)
            self.connect()

    def close(self):
        try:
            if(self.sr):
                self.sr.close()
                fmessage = '\nSerCon Close'
                print(fmessage)
                self.isConnected = False
        except Exception as e:
            fmessage = '\nError in Closing SerCon: ' + str(e)
            print(fmessage)


if __name__ == "__main__":
    print('In Serial TestConnection')
    ser = SerCon()
    ser.__init__()
    ser.connect()
    print('Trying to connect')
    ser.connect()
    print('Sleep for 2 seconds')
    time.sleep(1)
    while True:
        try:
            print('Enter a text')
            stext = input()
            ser.write(str(stext))
            rtext = ser.read()
            print(str(rtext))
        except KeyboardInterrupt:
            print('SerCon Interrupted')
            break
