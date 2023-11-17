package dto

type PlayerDTO struct {
	Username string
	Email    string
	Weight   int
	Age      int
}

type Message struct {
	PlayerDTO PlayerDTO
	Action    string
	GameType  string
	Message   string
}
