package game

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
)

func (app *App) GetService(service, path string, v any) error {
	host, err := app.reg.LookupHost(service)
	if err != nil {
		return err
	}

	uri := fmt.Sprintf("http://%s%s", host, path)

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
