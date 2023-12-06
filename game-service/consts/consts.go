package consts

const (
	GameServiceName      = "game-service"
	ChallengeServiceName = "challenge-service"
	PlayerServiceName    = "player-service"
	GEOServiceName       = "geo-service"
	HRMServiceName       = "hrm-service"
)

const (
	GameServiceExchange      = GameServiceName
	GameStartKey             = "game_start"
	PlayerToGameQueue        = "player->game"
	HRMStartKey              = "hrm_transmit"
	HRMToGameQueue           = "hrm->game"
	ChallengeServiceExchange = ChallengeServiceName
	GameToChallengeQueue     = "game->challenge"

	PlayerServiceExchange = PlayerServiceName
	GameStatusKey         = "game_status_response"
	GameToPlayerQueue     = "game->player"
)

const (
	ActionAddBadge = "ADD_BADGE"
)

const (
	HRMSendAction = "SEND_HEART_RATE"
)

const (
	ReactSheltering = "SHELTERING"
	ReactEscaping   = "ESCAPING"
	ReactFighting   = "FIGHTING"
)

const (
	AttackModeProf   = "Grumpy Prof"
	AttackModeBeaver = "Coot Beaver"
)

const (
	BadgeFighter = "badge_fighter"
	BadgeHider   = "badge_hider"
	BadgeRunner  = "badge_runner"
)
