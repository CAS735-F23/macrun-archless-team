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
	HRMStartKey         = "hrm_start"
	HRMToGameQueue      = "hrm->game"

	PlayerServiceExchange = PlayerServiceName
	GameStatusKey         = "game_status_response"
	GameToPlayerQueue     = "game->player"
)

const (
	GameStatusActionOK     = "game_status_action_ok"
	GameStatusActionFailed = "game_status_action_failed"
)

const (
	GameStartAction = "START_GAME"
	GameStopAction  = "STOP_GAME"
)
