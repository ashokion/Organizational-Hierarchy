import java.io.*; 
import java.util.*; 
import java.util.Collections;

// Tree node
class Node {
	int id;
	Vector<Node> children=new Vector<>();
	Node parent;
	int level;
	int size;
	Node(int data){
		this.id=data;
		this.children=new Vector<>();
		this.level=1;
		this.size=1;
	} 
}

class NodeA{
	NodeA right;
	NodeA left;
	int height;
	Node dupid;
}
class AVLTree{
	NodeA roota;
	AVLTree(Node n){
		this.roota=new NodeA();
		this.roota.dupid=n;
		this.roota.height=0;

	}
	
	public NodeA createNewNode(Node d){
		NodeA a = new NodeA();
		a.dupid=d;
		
		a.left=null;
		a.right=null;
		return a;

	}
	public int maxval(int x, int y){
		if( x > y ){
			return x;
		}
		return y;

	}
	public int height(NodeA node){
		if( node != null ){
			return node.height;
		}
		return -1;
	}
	public NodeA rotateright( NodeA y){
		NodeA x=y.left;
		NodeA t2=x.right;

		
		y.left= t2;
		x.right=y;
		int a=maxval(height(y.right) , height(y.left));
		y.height = 1 + a;
		int b=maxval(height(x.right) , height(x.left));
		x.height = 1 + b;



		return x;

	}

	public NodeA rotateleft(NodeA x){
		NodeA y=x.right;
		NodeA t2= y.left;

		x.right=t2;
		y.left=x;
		
		
		int b=maxval(height(x.right) , height(x.left));
		x.height = 1 + b;
		int a=maxval(height(y.right) , height(y.left));
		y.height = 1 + a;
		
		return y;

	}
	int getBalance(NodeA node) 
    { 
        if (node == null) 
            return 0; 
        return height(node.left) - height(node.right); 
    } 
	public Node search(NodeA temp,int id) throws IllegalIDException{
		//Node temp=roota.dupid;
		if(temp==null){
			throw new IllegalIDException("Id doesnt exist");
		}
		if(temp.dupid.id==id){
			return temp.dupid;

		}
		else if(temp.dupid.id>id){
			
			return search(temp.left,id);
		}
		else {
			return search(temp.right,id);
		}


		
		 	
	}
	public Node search(int id)throws IllegalIDException{
		return search(roota, id);
	}

	public NodeA insertA(NodeA node, Node dupid) throws IllegalIDException{
		
		if(node==null){
			return createNewNode(dupid);
		}
		if ( dupid.id > node.dupid.id ) {
			node.right = insertA( node.right , dupid );
		}
		
		else if (dupid.id < node.dupid.id ) {
			node.left = insertA(node.left, dupid);
		}
		else {
		    //return node;
			// throw exception employee with given id already exist
			throw new IllegalIDException("Node exists");
		}
		node.height = 1 + maxval ( height(node.left), height( node.right) ) ;
		//need to rebalance the tree

		int heightdiff = height(node.left)-height(node.right);

		//four possible combinations LL,LR,RL,RR
		//Left left 
		if(heightdiff > 1 && dupid.id < node.left.dupid.id){
			return rotateright(node);
		}
		//left right
		if(heightdiff > 1 && dupid.id > node.left.dupid.id){
			node.left= rotateleft(node.left);
			return rotateright(node);
		}
		//right right
		if(heightdiff < -1 && dupid.id > node.right.dupid.id){
			return rotateleft(node);
		}
		//right left
		if(heightdiff < -1 && dupid.id < node.right.dupid.id){
			node.right = rotateright(node.right);
			return rotateleft(node);
		}
		return node;


	}
	public void insert(Node n)throws IllegalIDException{
		roota=insertA(roota, n);
		

	}
	NodeA minValue(NodeA node){
		NodeA current=node;
		while(current.left!=null){
			current=current.left;
		}
		return current;
	}
	int numchild(NodeA node){
		
		if(node.left==null){
			if(node.right==null){
				return 0;
			}
			return 1;

		}
		else if(node.right==null){
			if(node.left==null){
				return 0;
			}
			return 1;

		}
		return 2;

	}
	NodeA deleteN(NodeA root, Node dupid) throws EmptyTreeException,IllegalIDException {
		
		//Node a=search(dupid.id);
		if(root.dupid==null){
			throw new IllegalIDException("Node doesnt exist");

		}
		else if(dupid.id<root.dupid.id){

			root.left = deleteN(root.left , dupid);
		}else if( dupid.id> root.dupid.id){
			root.right=deleteN(root.right , dupid);
		}else {
			int c=numchild(root);
			if(c==0){
				root=null;
			}else if(c==1){
				if(root.left==null){
					root=root.right;
				}
				else root=root.left;
			}else{
				NodeA temp =minValue(root.right);
				root.dupid =temp.dupid;
				root.right =deleteN(root.right , temp.dupid);

			}



			/*if((root.left==null)|| (root.right==null)){
				NodeA temp =null;
				if(temp == root.left){
					temp=root.right;
				}else{
					temp=root.left;
				}
				root=temp;
				
			}else{
				NodeA temp =minValue(root.right);
				root.dupid.id =temp.dupid.id;
				root.right =deleteN(root.right , temp.dupid);
			}*/




		}
		
		if(root ==null){
			return root;
		}
		root.height = 1 + maxval ( height(root.left), height( root.right) ) ;
		int balance=getBalance(root);
		// If this node becomes unbalanced, then there are 4 cases 
        // Left Left Case 
        if (balance > 1 && getBalance(root.left) >= 0) 
            return rotateright(root); 
  
        // Left Right Case 
        if (balance > 1 && getBalance(root.left) < 0) 
        { 
            root.left = rotateleft(root.left); 
            return rotateright(root); 
        } 
  
        // Right Right Case 
        if (balance < -1 && getBalance(root.right) <= 0) 
            return rotateleft(root); 
  
        // Right Left Case 
        if (balance < -1 && getBalance(root.right) > 0) 
        { 
            root.right = rotateright(root.right); 
            return rotateleft(root); 
        }
		return root;


	}
	public void delete(Node n) throws EmptyTreeException,IllegalIDException{
		if(roota==null){
			throw new EmptyTreeException("Tree is empty");
			

		}
		roota=deleteN(roota, n);
	}
	/*public String toString(NodeA n){
	    if(n==null)
		    return "";
		
		return Integer.toString(n.dupid.id)+"("+toString( n.left)+","+toString(n.right)+")";


	}
	public String toString(){
		return toString(roota);
	}*/

	


	
}


public class OrgHierarchy implements OrgHierarchyInterface{

//root node
Node root;
//NodeA roota;
AVLTree avlsearch;

public boolean isEmpty(){
	//your implementation
	
	if(root==null){
		return true;
	}
	return false;
} 

public int size(){
	//your implementation
	return root.size;
}

public int level(int id) throws IllegalIDException, EmptyTreeException{
	//your implementation
	Node employee=avlsearch.search(id);
	return employee.level;

} 

public void hireOwner(int id) throws NotEmptyException{
	//your implementation
	if(isEmpty()){
		root=new Node(id);
		//roota=new NodeA();
		avlsearch=new AVLTree(root);
		
	}
	else throw new NotEmptyException("");
}

public void hireEmployee(int id, int bossid) throws IllegalIDException, EmptyTreeException{
	//your implementation
	
	if(isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	Node n=avlsearch.search(bossid);
	Node newemployee=new Node(id);
	newemployee.parent=n;
	n.children.add(newemployee);
	newemployee.level=n.level+1;
	avlsearch.insert(newemployee);
	root.size+=1;


} 

public void fireEmployee(int id) throws IllegalIDException,EmptyTreeException{
	//your implementation
	if(isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	if(id==root.id){
		throw new IllegalIDException("Owner cannot be deleted");
	}
	

	Node fireemplo=avlsearch.search(id);
	
	avlsearch.delete( fireemplo);
	fireemplo.parent.children.remove(fireemplo);
	root.size=root.size-1;


}
public void fireEmployee(int id, int manageid) throws IllegalIDException,EmptyTreeException{
	//your implementation
	if(isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	Node fireemployee=avlsearch.search(id);
	Node newboss=avlsearch.search(manageid);
	if(id==root.id){
		throw new IllegalIDException("Owner cannot be deleted");
	}
	if(manageid==root.id){
		throw new IllegalIDException("boss can't be manageid");
	}
	if(fireemployee.level!=newboss.level){
		throw new IllegalIDException("New boss should be on same level");
	}
	for(int i=0;i<fireemployee.children.size();i++){
		Node n=fireemployee.children.elementAt(i);
		n.parent=newboss;
		newboss.children.add(n);
	}
	avlsearch.delete( fireemployee);
	Node n=fireemployee.parent;
	
	n.children.remove(fireemployee);
//	System.out.println(n.children.size());
	root.size=root.size-1;

} 

public int boss(int id) throws IllegalIDException,EmptyTreeException{
	//your implementation
	if(isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	if(id==root.id){
		return -1;
	}
	Node n=avlsearch.search(id);
	return n.parent.id;
}

public int lowestCommonBoss(int id1, int id2) throws IllegalIDException,EmptyTreeException{
	if(isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	if(id1==root.id||id2==root.id){
		return -1;
	}
	Node employee1=avlsearch.search(id1);
	Node employee2=avlsearch.search(id2);

	int leveldiff=employee1.level-employee2.level;
	if(leveldiff>0){
		while(leveldiff>0){
			employee1=employee1.parent;
			leveldiff--;

		}
	}else if(leveldiff<0){
		while(leveldiff<0){
			employee2=employee2.parent;
			leveldiff++;

		}	}
	
	while(employee1.parent.id!=employee2.parent.id){
		employee1=employee1.parent;
		employee2=employee2.parent;

	}
	
	return employee1.parent.id;

}
public Vector<Integer> BFS(Node n){
	Vector<Node> v=new Vector<>();
	Vector<Integer> v2=new Vector<>();
	v.add(n);
	int a=0;
	int last_level=n.level;

	while(a<v.size()){
		Node temp=v.get(a);
		v.addAll(temp.children);
		if(temp.level==last_level+1){
			v2.add(-1);
			last_level++;
		}
		v2.add(temp.id);
		a++;
	}
	return v2;
}

public String toString(int id) throws IllegalIDException, EmptyTreeException{
	//your implementation
	// throw new java.lang.UnsupportedOperationException("Not implemented yet.");
	if(isEmpty()){
		throw new EmptyTreeException("Tree is empty");
	}
	String s=new String();

	Node n=avlsearch.search(id);
	Vector<Integer> v=new Vector<>();
	v=BFS(n);
	v.add(-1);
	Vector<Integer> v2=new Vector<>();
	for(Integer i :v){
		if(i==-1){
			Collections.sort(v2);
			//add v2 in string
			for(Integer j :v2){
				s+=j.toString()+" ";
			}
			s=s.substring(0, s.length()-1)+",";
			//s.char(s.length()-1)=",";
			v2=new Vector<>();
			continue;

		}
		else{
			v2.add(i);
		}
	}
	return s.substring(0, s.length()-1);
}

}
