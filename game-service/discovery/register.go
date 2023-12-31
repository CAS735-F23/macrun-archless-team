package discovery

import (
	"errors"
	"net"
	"strconv"

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
	if r.cfg.Server.IP == "" {
		r.cfg.Server.IP = getPrimaryIP(r.cfg.Discovery.Host, r.cfg.Discovery.Port)
	}
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

func (r *Registry) LookupIPPort(name string) (string, int, error) {
	md, err := r.cli.GetService(vo.GetServiceParam{
		ServiceName: name,
	})
	if err != nil {
		return "", 0, err
	}

	if len(md.Hosts) == 0 {
		return "", 0, errors.New("empty hosts/instances")
	}

	return md.Hosts[0].Ip, int(md.Hosts[0].Port), nil
}

func (r *Registry) LookupHost(name string) (string, error) {
	host, port, err := r.LookupIPPort(name)
	return net.JoinHostPort(host, strconv.Itoa(port)), err
}
