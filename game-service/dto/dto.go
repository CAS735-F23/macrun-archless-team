package dto

type PlayerDTO struct {
	Username string
	Email    string
	Weight   int
	Age      int
}

type HeartRateDTO struct {
	Username  string
	HeartRate int
}

type ActionDTO struct {
	Action  string
	Message string
}
