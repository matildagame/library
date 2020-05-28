extends Control


# Declare member variables here. Examples:
# var a = 2
# var b = "text"

var network



var playerID=""

var status

export var body_texture="6"
export var hair_texture="7"
export var mesh="0"

# Called when the node enters the scene tree for the first time.
func _ready():
	network=$NetworkManager
	status=$Panel/VBoxContainer/Status


# Called every frame. 'delta' is the elapsed time since the previous frame.
#func _process(delta):
#	pass

func _on_Button_pressed():
	var nombre=$Panel/VBoxContainer/HBoxContainer/Nombre.text
	var direccion_servidor=$Panel/VBoxContainer/HBoxContainer6/Servidor.text
	var puerto_servidor=str($Panel/VBoxContainer/HBoxContainer5/Puerto.text)
	var habitacion=$Panel/VBoxContainer/HBoxContainer3/partidaID.text
	
	var lib_port=$Panel/VBoxContainer/HBoxContainer7/PuertoLib.text
	
	network.set_lib_port(int(lib_port))
	network.init()
	network.register(nombre,habitacion,mesh,body_texture,hair_texture,direccion_servidor,puerto_servidor)
	
	#yield(network.matildaLib,"registered_received")
	#print("Registrado")
	
	#yield(network.matildaLib,"start_match")
	#print("Registrado")
	
	#We ask for joining a match..
	#network.join(partida_id,network.matildaLib.token_id)
	# And wait until the response is received. Is it ok to block the application?
	#yield(network.matildaLib,"join_setup")
	#print("En partida..")
	


func _on_NetworkManager_register_reply(playerID_):
	playerID=playerID_
	set_status("PlayerID: "+playerID)
	print("----->")

func set_status(status_):
	status.text=status_

func _on_NetworkManager_start_match(list):
	set_status("Comienza partida con "+str(list.size())+" jugadores")


func _on_NetworkManager_players_list_update(list):
	var lista_jugadores=""
	
	for jugador in list:
		lista_jugadores+="/ "+jugador["username"]	
		
	set_status("> "+lista_jugadores)
