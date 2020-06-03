# Mason-Program
a program to calculate the over all
transfer function using Mason Formula.
Mason Formula:
Mason's gain formula (MGF) is a method for finding the transfer function of a linear signal-flow graph (SFG)
The gain formula is as follows:
where:
 G=y_out/y_in =(∑_(K=1)^N▒〖G_K Δ_K 〗)/Δ
Δ=1-∑▒L_i +∑▒〖L_i L_j-〗 ∑▒〖L_i L_j L_k  +⋯+〖(-1)〗^M ∑▒〖…+⋯〗〗

Δ = the determinant of the graph.
y_in = input-node variable
y_(out )= output-node variable
G = complete gain between yin and yout
N = total number of forward paths between yin and yout
G_K= path gain of the kth forward path between yin and yout
〖L 〗_I= loop gain of each closed loop in the system
L_i L_J = product of the loop gains of any two non-touching loops (no common nodes)
L_i L_J L_K= product of the loop gains of any three pairwise nontouching loops
Δ_K= the cofactor value of Δ for the kth forward path, with the loops touching the kth forward path removed. 

User Manual:
-The program is implemented in java with CMD interface
-To start the program just click on “run.bat”
-Enter your graph’s vertices separated by “;”
-Start entering the edges once per line as this 
(Start Vertex, End vertex , Weight)
-Type “exit” to stop entering edges
-Enter the input vertex
-Enter the output vertex
-Once you hit enter button your answers will appear (Forward paths, Individual loops , Non-touching loops , Deltas , Transfer Function)
-Also, your graph will appear in popup tab in graphical representation
