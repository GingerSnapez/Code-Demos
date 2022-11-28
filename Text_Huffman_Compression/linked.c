#include <stdio.h>
#include <stdlib.h> // malloc
#include "linked.h"

LinkedList* llCreate() {
  return NULL;
}

int llIsEmpty(LinkedList* ll) {
  return (ll == NULL);
}

void llDisplay(LinkedList* ll) {
  
  LinkedList* p = ll;
  printf("[");
  
  while (p != NULL) {
    printf("%d, ", (*p).value->weight);
    p = p->next;
  }
  printf("]\n");
}

void llAdd(LinkedList** ll, Node* newValue) {
  // Create the new node
  LinkedList* newNode = (LinkedList*)malloc(1 * sizeof(LinkedList));
  newNode->value = newValue;
  newNode->next = NULL;
  
  // Find the end of the list
  LinkedList* p = *ll;
	if (p == NULL) {  // Add first element
		*ll = newNode;  // This is why we need ll to be a **
	} else {
		while (p->next != NULL) {
		p = p->next;
	}
    
    // Attach it to the end
    p->next = newNode;
  }
}

void list_add_in_order(LinkedList** ll, Node* newValue) {
	
	// Create the new node
	LinkedList* newNode = (LinkedList*)malloc(1 * sizeof(LinkedList));
	newNode->value = newValue;
	newNode->next = NULL;
  
	// Find the end of the list
	LinkedList* p = *ll;
	if (p == NULL) {  // Add first element
		*ll = newNode;  // This is why we need ll to be a **

		} else if (p->value->weight > newNode->value->weight) {
		
		*ll = newNode;
		newNode->next = p;
	} else {

		while (p->next != NULL && p->next->value->weight <= newNode->value->weight) {
			p = p->next;

		}

		// Attach it to the end
		newNode->next = p->next;
		p->next = newNode;
	}
}

void llFree(LinkedList* ll) {
	LinkedList* p = ll;
	while (p != NULL) {
		LinkedList* oldP = p;
		p = p->next;
		free(oldP);
	}
}

Node* removeFirst(LinkedList** ll) {
	
	LinkedList* p = *ll;
	Node* val;

	if (p != NULL) {
		val = p->value;
		*ll = p->next;
	}

	return val;
}

/*int main() {
  
	LinkedList* l = llCreate();
	llDisplay(l);
	
	Node n1;
	n1.weight = 7;

	Node n2;
	n2.weight = 5;
	
	Node n3;
	n3.weight = 3;

	Node n4;
	n4.weight = 8;

	list_add_in_order(&l, n1);
	llDisplay(l);

	list_add_in_order(&l, n2);
	llDisplay(l);
	
	list_add_in_order(&l, n3);
	llDisplay(l);
	
	list_add_in_order(&l, n4);
	llDisplay(l);

  	llFree(l);

	return 0;
}*/
