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
	GameStatusAction       = "game_status_update"
	GameStatusActionOK     = "OK"
	GameStatusActionFailed = "failed"
)
