module libcore

go 1.26.0

toolchain go1.26.5

require (
	github.com/matsuridayo/libneko v1.0.0 // replaced
	github.com/miekg/dns v1.1.72
	github.com/oschwald/maxminddb-golang v1.13.1
	github.com/sagernet/quic-go v0.59.0-sing-box-mod.4
	github.com/sagernet/sing v0.8.11
	github.com/sagernet/sing-box v1.0.0 // replaced
	github.com/sagernet/sing-tun v0.8.11
	github.com/ulikunitz/xz v0.5.15
	golang.org/x/mobile v0.0.0
	golang.org/x/sys v0.47.0
)

replace github.com/matsuridayo/libneko => ../../libneko

replace github.com/sagernet/sing-box => ../../sing-box

replace golang.org/x/mobile => ./gomobile
