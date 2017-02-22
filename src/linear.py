#!/usr/bin/python

from mininet.topo import Topo
from mininet.net import Mininet
from mininet.util import dumpNodeConnections
from mininet.log import setLogLevel
from mininet.link import TCLink
from mininet.cli import CLI

class SingleSwitchTopo(Topo):
    "Single switch connected to n hosts."
    def build(self, n=2):
        linkops = dict(bw=10, delay='5ms',  max_queue_size=1000, use_htb=True)

        # create first 
        previous = self.addSwitch('s1')
        host = self.addHost('h1')
        self.addLink(host,previous, **linkops)

        # create the rest of the connections
        for h in range(n-1):
            switch = self.addSwitch('s%s' % (h+2) )
            host = self.addHost('h%s' % (h + 2))
            self.addLink(host, switch, **linkops)
            self.addLink(previous, switch, **linkops)
            previous = switch
            
def simpleTest():
    "Create and test a simple network"
    number_of_hosts = 10
    topo = SingleSwitchTopo(n=number_of_hosts)
    net = Mininet(topo,link = TCLink)
    net.start()
    print "Dumping host connections"
    dumpNodeConnections(net.hosts)
    print "Testing network connectivity"
    net.pingAll()

    ##################################################
    # here is where the custom cmds go for each host #
    ##                                              ##


    ##               ##
    # End custom cmds #
    ###################
    
    CLI(net)
    net.stop()
        
if __name__ == '__main__':
    # Tell mininet to print useful information
    setLogLevel('info')
    simpleTest()
