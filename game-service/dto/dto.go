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

type PointDTO struct {
	X float64
	Y float64
}

const (
	ReactSheltering = "SHELTERING"
	ReactEscaping   = "ESCAPING"
	ReactFighting   = "FIGHTING"
)

const (
	AttackModeProf   = "Grumpy Prof"
	AttackModeBeaver = "Coot Beaver"
)

var AttackModes = []string{AttackModeProf, AttackModeBeaver}

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
