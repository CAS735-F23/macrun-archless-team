package game

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"net/url"
)

func (app *App) GetService(service, path string, v any) error {
	host, err := app.reg.LookupHost(service)
	if err != nil {
		return err
	}

	uri, err := url.JoinPath("http://"+host, path)
	if err != nil {
		return err
	}

	log.Printf("Make GET Request to service %s: %s", service, uri)

	res, err := http.Get(uri)
	if err != nil {
		return err
	}
	defer res.Body.Close()

	if res.StatusCode != http.StatusOK {
		return fmt.Errorf("bad http status: %d", res.StatusCode)
	}

	return json.NewDecoder(res.Body).Decode(v)
}

func (app *App) PostService(service, path string, body any) {

}
