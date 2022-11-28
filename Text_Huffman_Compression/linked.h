typedef struct tnode {
	int weight;
	char c;
	struct tnode* left;
	struct tnode* right;
	struct tnode* parent;
} Node;

typedef struct node {
	Node* value;
	struct node* next;
} LinkedList;

LinkedList* llCreate();
int llIsEmpty(LinkedList* ll);
void llDisplay(LinkedList* ll);
void llAdd(LinkedList** ll, Node* newValue);
void list_add_in_order(LinkedList** ll, Node* newValue);
void llFree(LinkedList* ll);
Node* removeFirst(LinkedList** ll);
