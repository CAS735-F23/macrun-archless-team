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
	Action  string `json:"action"`
	Message string `json:"message"`
}

type BadgeActionDTO struct {
	ActionDTO
	BadgeAddRequest struct {
		Challenge string `json:"challenge"`
		Username  string `json:"username"`
		BadgeName string `json:"badgeName"`
	} `json:"badgeAddRequest"`
}

type PointDTO struct {
	X float64
	Y float64
}

type ActionResponseDTO struct {
	HeartRate int
	Speed     float64
	Score     int
	Attack    struct {
		On   bool
		Name string
	}
	Location PointDTO

	message string
}

func (r *ActionResponseDTO) GetMessage() string {
	return r.message
}

func (r *ActionResponseDTO) SetMessage(s string) {
	r.message = s
}

func (r *ActionResponseDTO) ResetMessage() {
	r.message = ""
}
