package discovery

import (
	"github.com/nacos-group/nacos-sdk-go/v2/clients"
	"github.com/nacos-group/nacos-sdk-go/v2/clients/naming_client"
	"github.com/nacos-group/nacos-sdk-go/v2/common/constant"
	"github.com/nacos-group/nacos-sdk-go/v2/vo"

	"game-service/config"
)

type Registry struct {
	cfg *config.Config
	cli naming_client.INamingClient
}

func New(cfg *config.Config) (*Registry, error) {
	sc := []constant.ServerConfig{
		*constant.NewServerConfig(cfg.Discovery.Host, uint64(cfg.Discovery.Port),
			constant.WithContextPath("/nacos")),
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

	return &Registry{
		cfg: cfg,
		cli: client,
	}, err
}

func (r *Registry) Register() error {
	_, err := r.cli.RegisterInstance(vo.RegisterInstanceParam{
		Ip:          r.cfg.Server.IP,
		Port:        uint64(r.cfg.Server.Port),
		ServiceName: r.cfg.Discovery.Service,
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

func (r *Registry) Deregister() error {
	_, err := r.cli.DeregisterInstance(vo.DeregisterInstanceParam{
		Ip:          r.cfg.Server.IP,
		Port:        uint64(r.cfg.Server.Port),
		ServiceName: r.cfg.Discovery.Service,
		Ephemeral:   true,
		//GroupName:   GroupName,
		//Cluster:     "",
	})
	return err
}
