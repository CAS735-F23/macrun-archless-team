package discovery

import (
	"sync"

	"github.com/nacos-group/nacos-sdk-go/v2/clients"
	"github.com/nacos-group/nacos-sdk-go/v2/clients/naming_client"
	"github.com/nacos-group/nacos-sdk-go/v2/common/constant"
	"github.com/nacos-group/nacos-sdk-go/v2/vo"

	"game-service/config"
)

const (
	ServiceName = "game.service"
	//GroupName   = "game.group"
)

var (
	_initOnce sync.Once
	_client   naming_client.INamingClient
)

func initClient(host string, port int) error {
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

	_client = client
	return nil
}

func Register(cfg *config.Config) (err error) {
	_initOnce.Do(func() {
		err = initClient(cfg.Discovery.Host, cfg.Discovery.Port)
	})
	if err != nil {
		return err
	}

	_, err = _client.RegisterInstance(vo.RegisterInstanceParam{
		Ip:          cfg.Server.IP,
		Port:        uint64(cfg.Server.Port),
		ServiceName: cfg.Discovery.Service,
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

func Deregister(cfg *config.Config) error {
	_, err := _client.DeregisterInstance(vo.DeregisterInstanceParam{
		Ip:          cfg.Server.IP,
		Port:        uint64(cfg.Server.Port),
		ServiceName: cfg.Discovery.Service,
		Ephemeral:   true, //it must be true
		//GroupName:   GroupName,
		//Cluster:     "",
	})
	return err
}
