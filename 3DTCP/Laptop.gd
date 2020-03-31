extends Area

var player

func _ready():
	# Get the player node using its single group "player"
	player=get_tree().get_nodes_in_group("player")[0]

func _on_Laptop_body_entered(body):

	print("- Laptop: someone collided me!")

	if (body==player):
		print("- Player: It's me!'")

	print("Entrando al terminal...")
	get_tree().change_scene("res://terminal.tscn")
