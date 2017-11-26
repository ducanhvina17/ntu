import bluetooth
import time


class BlueCon(object):
    def __init__(self):
        self.blueport = 4
        self.hostMacAdd = '08:60:6E:A5:9B:10'
        self.blueSocket = None
        self.isConnected = False

    def connect(self):
        try:
            self.blueSocket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
            fmessage = 'Bluetooth Socket = RFCOMM\n'
            print(fmessage)
            self.blueSocket.bind(("", self.blueport))
            fmessage = 'Bluetooth Binded\n'
            print(fmessage)
            self.blueSocket.listen(1)
            port = self.blueSocket.getsockname()[1]
            fmessage = 'Bluetooth Listen Completed\n'
            print(fmessage)

            # This is the UUID for the bluetooth. THis is from the website
            uuid = "00001101-0000-1000-8000-00805F9B34FB"

            bluetooth.advertise_service(self.blueSocket,
                                        "MDPGrp12",
                                        service_id=uuid,
                                        service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS],
                                        profiles=[bluetooth.SERIAL_PORT_PROFILE])

            fmessage = 'Waiting for connection on RFCOMM channel: ' + str(port)
            print(fmessage)

            self.client_sock, self.client_info = self.blueSocket.accept()
            print(self.client_sock)
            fmessage = '\nBluetooth Connected from ' + str(self.client_info)
            print(fmessage)

            self.isConnected = True
        except Exception as e:
            fmessage = '\nBluetooth Connection error: ' + str(e)
            print(fmessage)

    def read(self):
        try:
            receieved = self.client_sock.recv(2048)
            return receieved
        except Exception as e:
            fmessage = "\nBT Read Eror: " + str(e)
            fmessage = '\nRetrying to reconnect\n'
            print(fmessage)
            self.connect()

    def write(self, sMsg):
        try:
            text = str(sMsg)
            self.client_sock.send(text)
        except Exception as e:
            fmessage = 'Blue Write Error: ' + str(e)
            fmessage = '\nTrying to reconnect\n'
            print(fmessage)
            self.connect()

    def close(self):
        try:
            if self.client_sock:
                self.client_sock.close()
            if self.blueSocket:
                self.blueSocket.close()
            self.isConnected = False
            fmessage = 'BlueCon Close\n'
            print(fmessage)
        except Exception as e:
            fmessage = 'Error in Closing Bluetooth: ' + str(e)
            print(fmessage)


if __name__ == "__main__":
    print('In Blue Test Connection')
    blue = BlueCon()
    blue.__init__()
    blue.connect()
    time.sleep(1)
    blue.write('[provide status, F]')
    blue.write('[provide status, Test]')
    time.sleep(1)
    counter = 0
    while True:
        try:
            print('Enter a text')
            stext = 'From RPI'
            blue.write(str(stext) + str(counter))
            counter = counter + 1
            rtext = blue.read()
            print(str(rtext))
        except KeyboardInterrupt:
            print('PCCon Interrupted')
            break
    blue.close()
