package message

import (
	"context"
	"time"

	amqp "github.com/rabbitmq/amqp091-go"
)

func SendMessage(url, exchange, key, msg string) error {
	conn, err := amqp.Dial(url)
	if err != nil {
		return err
	}
	defer conn.Close()

	ch, err := conn.Channel()
	if err != nil {
		return err
	}
	defer ch.Close()

	err = ch.ExchangeDeclare(
		exchange,
		"topic",
		true,
		false,
		false,
		false,
		nil,
	)
	if err != nil {
		return err
	}

	ctx, cancel := context.WithTimeout(context.Background(), 30*time.Second)
	defer cancel()

	return ch.PublishWithContext(ctx,
		exchange,
		key,
		false,
		false,
		amqp.Publishing{
			DeliveryMode: amqp.Persistent,
			Timestamp:    time.Now(),
			ContentType:  "text/plain",
			Body:         []byte(msg),
		})
}
