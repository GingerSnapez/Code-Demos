#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "linked.h"

//to do:
//removeFirst
//make tree
//encode
//decode
//free

Node* createFreqTable(char* fName);
Node* createHuffmanTree(Node* t); 
void encodeFile(char* argv, Node* t);
void decodeFile(char* argv, Node* t); 

int main(int argc, char *argv[]) {
	
	// Check the make sure the input parameters are correct
	if (argc != 3) {
		printf("Error: The correct format is \"hcompress -e filename\" or \"hcompress -d filename.huf\"\n"); 
		fflush(stdout);
		exit(1);
	}
	
	// Create the frequency table by reading the generic file
	Node* leafNodes = createFreqTable("test.txt");

	//Create the huffman tree from the frequency table
	Node* treeRoot = createHuffmanTree(leafNodes);

	// encode
	if (strcmp(argv[1], "-e") == 0) {
		// Pass the leafNodes since it will process bottom up
		encodeFile(argv[2], leafNodes);

	} else { // decode
		// Pass the tree root since it will process top down
		decodeFile(argv[2], treeRoot);
	} 

	for (int i=0; i<128; i++) {
		free(&leafNodes[i]);
	}
	free(leafNodes);

	return 0;
}

Node* createFreqTable(char* fName) {

	Node* table = (Node*)malloc(128 * sizeof(Node));
	
	//Initialize all table variables
	for (int i = 0; i < 128; i++) {
		table[i].weight = 0;
		table[i].c = i;
		table[i].parent = NULL; 
		table[i].left = NULL;
		table[i].right = NULL; 
	}

	// Open file and add weights
	FILE* fp = fopen (fName, "r");
	char c;
	while((c = fgetc(fp)) != EOF) {
		table[c].weight += 1;
	}

	/*
	// print all frequencies
	for (int i=0; i<128; i++) {
		printf("%d: %d\n", table[i].c, table[i].weight);
	}
	*/

	return table;
}

Node* createHuffmanTree(Node* t) {

	LinkedList* ordered = llCreate();
	int count = 0;

	//one loop to add them
	for (int i = 0; i < 128; i++) {
		list_add_in_order(&ordered, &t[i]);
		count++;
	}
	
	// Temp node variables
	Node* left;
	Node* right;
	Node* parent;

	//add together under a node that only has weight and char -1
	while (count > 1) {

		// assign variables
		parent = (Node*)malloc(1*sizeof(Node));
		left = removeFirst(&ordered);
		right = removeFirst(&ordered);

		// assign parent and children
		parent->weight = left->weight + right->weight;
		left->parent = parent;
		right->parent = parent;
		parent->left = left;
		parent->right = right;
		parent->c = -1;
		
		count--;
		list_add_in_order(&ordered, parent);
	}

	t = ordered->value;
	llFree(ordered);

	return t;
}

void encodeFile(char* fName, Node* t) {

	// open files
	FILE* fpr = fopen (fName, "r");
	FILE* fpw = fopen ("encoded.huf", "w");

	unsigned char byte = 0;	// stores byte to be written
	char c;	// stores character read in
	int index = 7;	// used for bit manipulation
	int path[128];	// stores path
	int i = 0;	// size of temp
	int sentinel = 1; // boolean to add sentinel on last run
	Node* tt;

	while ((c = fgetc(fpr)) != EOF) {	
		tt = &t[c];

		// Find path
		while (tt->parent != NULL) {
			
			if (tt->parent->right == tt) {
				path[i] = 1;
			} else {
				path[i] = 0;
			}
			
			i++;
			tt = tt->parent;
		}

		while (i > 0) {

			//printf("%d", temp[i-1]);

			if (path[i-1] == 1) {	
				byte = byte|(1<<index);
			}

			i--;
			index--;

			if (index == -1) {
				fputc(byte, fpw);
				//printf("\n");
				byte = 0;
				index = 7;
			}
		}
		i = 0;

		// Add path to sentinel code (ASCII 4) on last run
		if ((c = fgetc(fpr)) == EOF && sentinel == 1) {
			c = 4;
			sentinel = 0;
		}
		ungetc(c, fpr);
	}
	
	// add remaining characters
	if (byte != 0) {
		/*
		for (int x=0; x<index+1; x++)
			printf("0");
		printf("\n");
		*/
		fputc(byte, fpw);
	}	
}


void decodeFile(char* fName, Node* root) {

	FILE* fpr = fopen (fName, "r");
	char byte = fgetc(fpr); // read first char
	int bit;	// temp bit for traversal
	int index = 7; // for bit manipulation
	Node* tt;

	// Stop when you find sentinel node (ASCII 4)
	while(tt->c != 4) {

		tt = root;
		
		// While not at leaf (all interior node char = -1)
		while (tt->c == -1) {

			// follow path
			bit = (byte>>index)&1;
			if (bit == 1) {
				tt = tt->right;
			} else {
				tt = tt->left;
			}

			index--;

			// read new byte
			if (index == -1) {
				byte = fgetc(fpr);
				index = 7;
			}
		}

		// print character (prints total file)
		printf("%c", tt->c);
	}
}

//everytime you remove first free, array of leaf nodes only takes one free, tree requires recursive tree reversal to free

//new file, decoded.txt (command dif)

//partner and everyhting that isn't working
