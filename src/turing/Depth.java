package turing;
//https://github.com/codageaider/Crack-The-Coding-Interview/tree/main/src/turing
public class Depth {
    /*
    1. edge cases
    2. Algorithm efficency.

    Time Complexity -
    T(root) = 1+T(root.left) + T(root.right)
    T(root) who many steps do my this recusive solution takes.
    T(root) = 1+ 1+ T(root.left.left) + T(root.left.right) + 1 + T(root.right.left)+
    T(root.right.right)

    MaxDepth of a tree is K
    T(root.left.left.right.left...) = 0
    At each depth a 1 will be added

    Lets consider a tree which has just 3 nodes.
    T(leaf) = 1 , T(leaf) = 1
    1+ 1+1+1 <-- number of steps taken to calculate the max depth of tree = 4.
    1
   / \
  2   3
  ans = 2  #Step = 4
  If a tree has maxdepth =k and the tree has n nodes.
  T(root) = # steps to calculate the maxDepth =
  >=n
  O(n) <-- n is the number of nodes in the tree

  Observation: You can calculate the depth of a node/subtree in 2 additional steps
  once you have determined the depth of all nodes in the child tree

     */
    public static void main(String[] args) {
        TreeNode root= new TreeNode(1);

    }
    /**
     *
     * @param root : root node of the tree
     * @return  max depth of this tree
     *
     * Algorithm:
     * 1. calculate the max-depth of left subtree
     * 2. calculate the max-depth of the right subtree
     * 3. return 1+ max of the value return in 1 and 2
     */
    public static int maxDepth(TreeNode root){
        if(root==null)
            return 0;
        return Math.max(maxDepth(root.left),maxDepth(root.right))+1;
    }
}

class TreeNode{
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int val){this.val=val;}
}
