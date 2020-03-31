extends KinematicBody

var server
var connection = {}

export var port=9999
export var dirIP="127.0.0.1"

var client=null
var buffer=""

const speed = 10
var move = 0

func _ready():
	add_to_group("units")
	
	server = TCP_Server.new()
	# La funcion listen hace que el server TCP escuche por el puerto Indicado (O-> Todo funciona Okay)
	if server.listen(port,dirIP) == 0:
		print("Server started at port "+str(port) + "\n")
	set_process(true)


 
func _process(delta):

	# Returns true if a connection is available for taking.
	if server.is_connection_available():
		# accept connection and assign it to the client var
		# Client as 'StreamPeerTCP' returned by a TCP server
		client = server.take_connection() 
		print("New client connected\n")
	
	if client!= null:
		# The cliente (somehow) is not connected anymore
		if !client.is_connected_to_host(): 
			print("Client disconnected "+str(client.get_status()))
		# If if indeed connected...
		else:
			# Read a line from the socket
			# ------------------------------------------------------------------
			# Gets the available bytes in the channel
			var n = client.get_available_bytes()
			# If there is some data...
			if n>0:
#				var datos=client.get_data(n)
#				var pool=PoolByteArray(datos[1])
#				# Get the information as string format 
#				buffer=buffer+pool.get_string_from_ascii();
#				var 
				var linea = client.get_string(n)
				linea =  linea.left((linea.find("\n")-1))
				# ------------------------------------------------------------------
				# ------------------------------------------------------------------
				print(">> " + linea)
				# Update buffer to empty at each new entrance
				buffer=""
				# ------------------------------------------------------------------
				# Process
				# ------------------------------------------------------------------
				var answer
				if linea == "d":
					move = 1
					print("Moviendo a la derecha")
					answer="ok"
				elif linea == "a":
					move = -1
					print("Moviendo a la Izquierda")
					answer="ok"
				else:
					answer="error!"
					print("Comando recibido no valido")
				# ------------------------------------------------------------------
				# ------------------------------------------------------------------
				# Write the answer over the socket
				answer=answer+'\n'
				client.put_data(answer.to_ascii())
				
#				client.put_u32(50)

	
	# Update velocity vector and move!
	var velocity = Vector3()  # The player's movement vector.
	velocity.z += move
		
	if velocity.length() > 0:
		velocity = velocity.normalized() * speed
		
	move_and_slide(velocity,Vector3(0,1,0));
	
	
	
func receive_linea():
	
	var linea
	
	# Gets the available bytes in the channel
	var n = client.get_available_bytes()
	# If there is some data...
	if n>0:
		var datos=client.get_data(n)
		var pool=PoolByteArray(datos[1])	
		# Get the information as string format 
		buffer=buffer+pool.get_string_from_utf8();
		linea =  buffer.left((buffer.find("\n")-1))

	return linea
