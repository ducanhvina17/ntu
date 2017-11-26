import socket
import time


class PCCon(object):
    def __init__(self):
        self.IpAdd = '192.168.12.12'
        self.port = 6666
        self.serverSocket = None
        self.pcClient = None
        self.isConnected = False

    def connect(self):
        try:
            self.serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            fmessage = '\nPC Socket done'
            print(fmessage)

            # Resuable address
            self.serverSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
            self.serverSocket.bind((self.IpAdd, self.port))
            fmessage = '\nPC Binded'
            print(fmessage)

            self.serverSocket.listen(1)
            fmessage = '\nPC waiting for connection'
            print(fmessage)

            self.pcClient, self.pcClientIP = self.serverSocket.accept()
            fmessage = '\nPC connected from ' + str(self.pcClientIP)
            print(fmessage)

            self.isConnected = True
        except Exception as e:
            fmessage = '\nPCCon Error: ' + str(e)
            print(fmessage)

    def read(self):
        try:
            message = ''
            msg = self.pcClient.recv(1)
            if (msg != ''):
                message += msg
                while(msg != '\n'):
                    msg = self.pcClient.recv(1)
                    message += msg
                return message
            else:
                fmessage = "\nHost disconnect"
                print(fmessage)
                self.isConnected = False
                fmessage = "\nTrying to reconnect"
                print(fmessage)
                self.connect()
        except Exception as e:
            fmessage = '\nPC read Error: ' + str(e)
            print(fmessage)
            self.connect()

    def write(self, sMsg):
        try:
            if self.isConnected:
                self.pcClient.sendto(sMsg.encode(), self.pcClientIP)
            else:
                fmessage = '\nHost Disconnected'
                print(fmessage)
                self.isConnected = False
                fmessage = '\nTrying to Reconnect'
                print(fmessage)
                self.connect()
        except Exception as e:
            fmessage = '\nPC write Error: ' + str(e)
            print(fmessage)
            self.connect()
            self.write(sMsg)

    def close(self):
        try:
            if self.serverSocket:
                self.serverSocket.close()
                fmessage = '\nPCCon Server Socket Close'
                print(fmessage)
            if self.pcClient:
                self.pcClient.close()
                fmessage = '\nPCCon Client Close'
                print(fmessage)
            self.isConnected = False
        except Exception as e:
            fmessage = '\nError in Closing PCCon: ' + str(e)
            print(fmessage)

if __name__ == "__main__":
    print('In PC Test Connection')
    PC = PCCon()
    PC.__init__()
    PC.connect()
    counter = 0
    while True:
        try:
            print('Enter a text')
            stext = 'From RPI'
            PC.write(str(stext) + str(counter))
            counter = counter + 1
            time.sleep(0.2)
        except KeyboardInterrupt:
            print('PCCon Interrupted')
            break
    PC.close()
