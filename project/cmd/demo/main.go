package main

import (
	"fmt"
	"github.com/everFinance/coding-styleguide/project/demo"
	"github.com/urfave/cli/v2"
	"log"
	"os"
	"os/signal"
	"syscall"
)

func main() {
	app := &cli.App{
		Name:    "demo",
		Version: "v1.3.5",
		Flags: []cli.Flag{
			&cli.StringFlag{Name: "ar_node", Value: "https://arweave.net", EnvVars: []string{"AR_NODE"}},
			&cli.StringFlag{Name: "mysql", Value: "root@tcp(127.0.0.1:3306)/demo_dev`?charset=utf8mb4&parseTime=True&loc=Local", Usage: "mysql dsn", EnvVars: []string{"MYSQL"}},
		},
		Action: run,
	}

	err := app.Run(os.Args)
	if err != nil {
		log.Fatal(err)
	}
}

func run(c *cli.Context) error {
	fmt.Printf("App-Name:%s, App-Version:%s \n", c.App.Name, c.App.Version)

	signals := make(chan os.Signal, 1)
	signal.Notify(signals, os.Interrupt, syscall.SIGTERM)

	dm := demo.New(c.String("ar_node"), c.String("mysql"))

	dm.Run()

	<-signals

	dm.Close()

	return nil
}
