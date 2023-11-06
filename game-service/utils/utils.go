package utils

import (
	"net"
	"strconv"
)

func GetPrimaryIP(ip string, port int) string {
	c, err := net.Dial("tcp", net.JoinHostPort(ip, strconv.Itoa(port)))
	if err != nil {
		return ""
	}
	return c.LocalAddr().(*net.TCPAddr).IP.String()
}
