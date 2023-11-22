package consts

const (
	GameServiceName      = "game-service"
	ChallengeServiceName = "challenge-service"
	PlayerServiceName    = "player-service"
	GEOServiceName       = "geo-service"
	HRMServiceName       = "hrm-service"
)

const (
	GameServiceExchange = GameServiceName
	GameStartKey        = "game_start"
	PlayerToGameQueue   = "player->game"
	HRMStartKey         = "hrm_transmit"
	HRMToGameQueue      = "hrm->game"

	PlayerServiceExchange = PlayerServiceName
	GameStatusKey         = "game_status_response"
	GameToPlayerQueue     = "game->player"
)

const (
	HRMSendAction = "SEND_HEART_RATE"
)
