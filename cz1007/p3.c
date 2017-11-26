//////////////////////////////////////////////////////////////////////////////////

/* CX1007 Data Structures
2015/16 S1
Author and Lab Group: Lee Xing Zhao FSP2
Program name: FSP2_LeeXingZhao.c
Date: 13 November 2015
Purpose: Implementing the required functions for Assignment 1 (Question 3)*/

//////////////////////////////////////////////////////////////////////////////////
#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>


//////////////////////////////////////////////////////////////////////////////////

typedef struct _listnode
{
	int item;
	struct _listnode *next;
} ListNode;	// You should not change the definition of ListNode

typedef struct _linkedlist
{
	int size;
	ListNode *head;
} LinkedList;	// You should not change the definition of LinkedList

typedef struct _queue
{
	LinkedList ll;
} Queue;	// You should not change the definition of Queue


			///////////////////////// function prototypes ////////////////////////////////////

			// These are for question 3. You should not change the prototype of these functions
void createQueueFromLinkedList(LinkedList *ll, Queue * q);
void removeEvenValues(Queue *q);


// You may use the following functions or you may write your own
void printList(LinkedList *ll);
void removeAllItems(LinkedList *ll);
ListNode * findNode(LinkedList *ll, int index);
int insertNode(LinkedList *ll, int index, int value);
int removeNode(LinkedList *ll, int index);
void removeAllItemsFromQueue(Queue *q);
void enqueue(Queue *q, int item);
int dequeue(Queue *q);
int isEmptyQueue(Queue *q);

//////////////////////////// main() //////////////////////////////////////////////

int main()
{
	int c, i, j;
	LinkedList ll;
	Queue q;

	j = -1;

	// Initialize the linked list as an empty linked list
	ll.head = NULL;
	ll.size = 0;

	// Initialize the queue as an empty queue
	q.ll.head = NULL;
	q.ll.size = 0;

	printf("1: Insert an integer into the linked list:\n");
	printf("2: Create queue from the linked list:\n");
	printf("3: Remove even numbers from queue:\n");
	printf("0: Quit:\n");
	printf("Please input your choice(1/2/3/0): ");
	scanf("%d", &c);

	while (c != 0)
	{
		switch (c)
		{
			case 1:
				printf("Input an integer that you want to add to the linked list: ");
				scanf("%d", &i);
				insertNode(&ll, ll.size, i);
				printf("The resulting linked list is: ");
				printList(&ll);
				break;
			case 2:
				createQueueFromLinkedList(&ll, &q); // You need to code this function
				printf("The resulting queue is: ");
				printList(&q.ll);
				break;
			case 3:
				removeEvenValues(&q); // You need to code this function
				printf("The resulting queue after removing even integers is: ");
				printList(&q.ll);
				break;
			case 0:
				removeAllItems(&ll);
				removeAllItemsFromQueue(&q);
				break;
			default:
				printf("Choice unknown;\n");
				break;
		}

		printf("\nPlease input your choice(1/2/3/0): ");
		scanf("%d", &c);
	}

	return 0;
}


//////////////////////////////////////////////////////////////////////////////////

// Create a queue from the LinkedList
void createQueueFromLinkedList(LinkedList *ll, Queue * q)
{
	ListNode *cur;

	if (ll->size <= 0) return;

	// Clear the merged list if it is not null
	if (isEmptyQueue(q))
		removeAllItemsFromQueue(q);

	// Set current node to first node
	cur = ll->head;

	// Enqueue all the nodes inside the LinkedList
	while (cur != NULL)
	{
		enqueue(q, cur->item);
		cur = cur->next;
	}

	// Clear linked list
	removeAllItems(ll);
}

// Remove all even values
void removeEvenValues(Queue *q)
{
	int i;

	if (isEmptyQueue(q)) return;

	// Get the size of queue
	i = q->ll.size;

	// Check all the items in the node and remove if the item has even value
	while (i > 0)
	{
		// Add the item to the back if it doesn't has even value
		if (q->ll.head->item % 2 != 0)
			enqueue(q, q->ll.head->item);

		// Remove first item from the queue
		dequeue(q);

		i--;
	}
}

//////////////////////////////////////////////////////////////////////////////////



void removeAllItemsFromQueue(Queue *q)
{
	int count, i;
	if (q == NULL)
		return;
	count = q->ll.size;

	for (i = 0; i < count; i++)
		dequeue(q);
}

void printList(LinkedList *ll)
{
	ListNode *cur;
	if (ll == NULL)
		return;
	cur = ll->head;
	if (cur == NULL)
		printf("Empty");
	while (cur != NULL)
	{
		printf("%d ", cur->item);
		cur = cur->next;
	}
	printf("\n");
}

void removeAllItems(LinkedList *ll)
{
	ListNode *cur = ll->head;
	ListNode *tmp;

	while (cur != NULL) {
		tmp = cur->next;
		free(cur);
		cur = tmp;
	}
	ll->head = NULL;
	ll->size = 0;
}

ListNode * findNode(LinkedList *ll, int index)
{
	ListNode *temp;

	if (ll == NULL || index < 0 || index >= ll->size)
		return NULL;

	temp = ll->head;

	if (temp == NULL || index < 0)
		return NULL;

	while (index > 0) {
		temp = temp->next;
		if (temp == NULL)
			return NULL;
		index--;
	}

	return temp;
}

int insertNode(LinkedList *ll, int index, int value)
{
	ListNode *pre, *cur;

	if (ll == NULL || index < 0 || index > ll->size + 1)
		return -1;

	// If empty list or inserting first node, need to update head pointer
	if (ll->head == NULL || index == 0) {
		cur = ll->head;
		ll->head = malloc(sizeof(ListNode));
		ll->head->item = value;
		ll->head->next = cur;
		ll->size++;
		return 0;
	}

	// Find the nodes before and at the target position
	// Create a new node and reconnect the links
	if ((pre = findNode(ll, index - 1)) != NULL) {
		cur = pre->next;
		pre->next = malloc(sizeof(ListNode));
		pre->next->item = value;
		pre->next->next = cur;
		ll->size++;
		return 0;
	}

	return -1;
}

int removeNode(LinkedList *ll, int index)
{
	ListNode *pre, *cur;

	// Highest index we can remove is size-1
	if (ll == NULL || index < 0 || index >= ll->size)
		return -1;

	// If removing first node, need to update head pointer
	if (index == 0) {
		cur = ll->head->next;
		free(ll->head);
		ll->head = cur;
		ll->size--;

		return 0;
	}

	// Find the nodes before and after the target position
	// Free the target node and reconnect the links
	if ((pre = findNode(ll, index - 1)) != NULL) {

		if (pre->next == NULL)
			return -1;

		cur = pre->next;
		pre->next = cur->next;
		free(cur);
		ll->size--;
		return 0;
	}

	return -1;
}

void enqueue(Queue *q, int item)
{
	insertNode(&(q->ll), q->ll.size, item);
}

int dequeue(Queue *q)
{
	int item;

	if (!isEmptyQueue(q)) {
		item = ((q->ll).head)->item;
		removeNode(&(q->ll), 0);
		return item;
	}
	return -1;
}

int isEmptyQueue(Queue *q)
{
	if ((q->ll).size == 0)
		return 1;
	return 0;
}
