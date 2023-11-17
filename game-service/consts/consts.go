package consts

const (
	GameServiceExchange = "game.service"
	GameStartKey        = "game_start"
	PlayerToGameQueue   = "player->game"
	HRMStartKey         = "hrm_start"
	HRMToGameQueue      = "hrm->game"

	PlayerServiceExchange = "player.service"
	GameStatusKey         = "game_status_response"
	GameToPlayerQueue     = "game->player"
)

const (
	GameStatusActionOK     = "game_status_action_ok"
	GameStatusActionFailed = "game_status_action_failed"
)

const (
	GameStartAction = "START_GAME"
)
