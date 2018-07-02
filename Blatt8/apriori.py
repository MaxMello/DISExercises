import numpy as np
import itertools

Threshold = 0.01

def loadTransactions():
    file = open("transactions.txt", "r")
    transactions = [] #np.array([])
    for line in file:
        transactions.append([int(x) for x in line.split()]) #np.array([int(x) for x in line.split()]))   
    file.close()
    return transactions

#print(loadTransactions())

def loadItems(transactions):
    items = set()
    for transaction in transactions:
        for item in transaction:
            items.add(item)
    return items

#print(loadItems(loadTransactions()))
    

def supportOfSet(s, T):
    # TODO how many transactions in T contain the set s
    totalNrOfSets = T.__len__()
    nrOfOccurrence = 0
    #occurrence = np.array([])
    for transaction in T:
        if set(s).issubset(set(transaction)):
            #np.append(occurrence, transaction)
            nrOfOccurrence = nrOfOccurrence + 1
    support = nrOfOccurrence/totalNrOfSets
    return support


def frequentItemset(support, threshold):
    return (support >= threshold)


def findFrequent1Itemsets(I, T, threshold):
    frequentItems = []
    for item in I:
        #calculate
        #print(item)
        support = supportOfSet(np.array([item]), T)
        #print("support:", support)
        isFreqent = frequentItemset(support, threshold)
        #print("frequent:", isFreqent)
        if isFreqent:
            frequentItems.append([item])
    return frequentItems


def findFrequentMultipleItemsSets(I, T, threshold):
    frequentItems = []
    for item in I:
        #calculate
        #print(item)
        support = supportOfSet(item, T)
        #print("support:", support)
        isFreqent = frequentItemset(support, threshold)
        #print("frequent:", isFreqent)
        if isFreqent:
            frequentItems.append(item)
    return frequentItems

def k1subsets(arr):
    return [list(x) for x in itertools.combinations(arr, len(arr)-1)]

#print(k1subsets([1, 2, 3, 4, 5, 6]))

def prune(c, frequentItems):
    subSets = k1subsets(c)
    for s in subSets:
        if not(s in frequentItems):
            return True
            #71631
    return False

#def getSets(frequentItems, nrOfItemsInSet):
def generateCandidates(frequentItems, k):
    k = k - 1 #TODO
    candidates = []
    for item1 in frequentItems:
        #print("item1",item1)
        for item2 in frequentItems:
            #print("item2",item2)
            (item1[:-1]==item2[:-1])
            (item1[k-1]>item2[k-1])
            if ((item1[:-1]==item2[:-1]) & (item1[k-1]<item2[k-1])):
                c = item1+[item2[k-1]]
                #print("c",c)
                #are all (k-1)-item subsets frequent itemsets?
                if not(prune(c, frequentItems)):
                    candidates.append(c)
    return candidates
    
    
    
def apriori(threshold):
    T = loadTransactions()
    I = loadItems(T)
    
    frequentItems = findFrequent1Itemsets(I, T, threshold)
    print("There are exactly ", len(frequentItems) , " frequent itemsets containing one item:")
    print(frequentItems)
    
    nrOfItemsInSet = 1
    
    while len(frequentItems)>0:
        nrOfItemsInSet = nrOfItemsInSet + 1
        #get List of all combinations of frequentItems
        candidates = generateCandidates(frequentItems, nrOfItemsInSet) #getSets(frequentItems, nrOfItemsInSet)
        #print(candidates)
        #print(len(candidates))
        frequentItems2 = findFrequentMultipleItemsSets(candidates, T, threshold)
        print("There are exactly ", len(frequentItems2) , " frequent itemsets containing ", nrOfItemsInSet," items:")
        print(frequentItems2)
        frequentItems = frequentItems2
    

apriori(Threshold)




        


    

    
    



