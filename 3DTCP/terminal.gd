extends Control

var lineaComandos

func _ready():
	lineaComandos = $LineaComandos/LineEdit




func _on_Button_pressed():
	print("Saliendo del terminal...")
	get_tree().change_scene("res://World.tscn")


func _on_LineEdit_text_entered(new_text):
	print("Comando Enviado...")
	lineaComandos.clear()
	
