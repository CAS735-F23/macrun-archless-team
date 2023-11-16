package message

import (
	"context"

	amqp "github.com/rabbitmq/amqp091-go"
)

type MQ struct {
	exchange, queue string

	conn *amqp.Connection
	ch   *amqp.Channel
	msgs <-chan amqp.Delivery
}

func New(url, exchange, queue string) (*MQ, error) {
	conn, err := amqp.Dial(url)
	if err != nil {
		return nil, err
	}

	ch, err := conn.Channel()
	if err != nil {
		return nil, err
	}

	err = ch.ExchangeDeclare(
		exchange,
		"direct",
		true,
		false,
		false,
		false,
		nil,
	)
	if err != nil {
		return nil, err
	}

	err = ch.QueueBind(
		queue,
		"",
		exchange,
		false,
		nil)

	msgs, err := ch.Consume(
		queue,
		"",
		true,
		false,
		false,
		false,
		nil,
	)

	return &MQ{
		exchange: exchange,
		queue:    queue,
		conn:     conn,
		ch:       ch,
		msgs:     msgs,
	}, nil
}

func (mq *MQ) MessageQueue() <-chan amqp.Delivery {
	return mq.msgs
}

func (mq *MQ) ReceiveMessage() string {
	return string((<-mq.msgs).Body)
}

func (mq *MQ) SendMessage(msg string) error {
	return mq.ch.PublishWithContext(context.Background(),
		mq.exchange,
		"",
		false,
		false,
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(msg),
		})
}

func (mq *MQ) Close() {
	_ = mq.ch.Close()
	_ = mq.conn.Close()
}
