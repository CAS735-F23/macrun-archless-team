package discovery

import (
	"sync"

	"github.com/nacos-group/nacos-sdk-go/v2/clients"
	"github.com/nacos-group/nacos-sdk-go/v2/clients/naming_client"
	"github.com/nacos-group/nacos-sdk-go/v2/common/constant"
	"github.com/nacos-group/nacos-sdk-go/v2/vo"
)

const (
	ServiceName = "game.service"
	//GroupName   = "game.group"
)

var (
	_once   sync.Once
	_client naming_client.INamingClient
)

func InitClient(host string, port int) error {
	sc := []constant.ServerConfig{
		*constant.NewServerConfig(host, uint64(port), constant.WithContextPath("/nacos")),
	}

	cc := *constant.NewClientConfig(
		constant.WithNamespaceId(""),
		constant.WithTimeoutMs(5000),
		constant.WithNotLoadCacheAtStart(true),
		constant.WithLogLevel("debug"),
	)

	client, err := clients.NewNamingClient(
		vo.NacosClientParam{
			ClientConfig:  &cc,
			ServerConfigs: sc,
		},
	)

	if err != nil {
		return err
	}

	_once.Do(func() {
		_client = client
	})
	return nil
}

func Register(ip string, port int) error {
	_, err := _client.RegisterInstance(vo.RegisterInstanceParam{
		Ip:          ip,
		Port:        uint64(port),
		ServiceName: ServiceName,
		Weight:      10,
		Enable:      true,
		Healthy:     true,
		Ephemeral:   true,
		//GroupName:   GroupName,
		//ClusterName: "",
		//Metadata:    map[string]string{},
	})
	return err
}

func Deregister(ip string, port int) error {
	_, err := _client.DeregisterInstance(vo.DeregisterInstanceParam{
		Ip:          ip,
		Port:        uint64(port),
		ServiceName: ServiceName,
		Ephemeral:   true, //it must be true
		//GroupName:   GroupName,
		//Cluster:     "",
	})
	return err
}
