from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer

class HelloWorldWeb(BaseHTTPRequestHandler):
    def do_GET(self):
        print "Got Request for \'{0}\'".format(self.path)

        try:
            self.send_response(200)
            self.send_header('Content-type',	'text/html')
            self.end_headers()

#            self.wfile.write("<html><head><title>Title</title></head>")
#            self.wfile.write("<body>Body")
#            self.wfile.write("</body></html>")
            self.wfile.write("Request for path {0}".format(self.path))
            self.wfile.close()
            return
                
        except IOError:
            self.send_error(404,'File Not Found: %s' % self.path)
     

    def do_POST(self):
        self.send_response(301)            
        self.end_headers()

def main():
    try:
        server = HTTPServer(('', 8080), HelloWorldWeb)
        print "Hello World Wide Web..."
        server.serve_forever()

    except KeyboardInterrupt:
        print "Kill received, shutting down"
        server.socket.close()

if __name__ == '__main__':
    main()

