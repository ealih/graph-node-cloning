# WRITE A METHOD TO CLONE AN ENTITY AND ALL ITS RELATED ENTITIES

An Entity is defined as a structure with the following three fields:

 - ID
 - Name
 - Description (optional)

Related Entities are represented as links from one Entity to another. A link entry has the following fields:

 - From Entity ID
 - To Entity ID

Note: Links are directional, i.e. they go from one entity to another. You can think of this as a directed graph where Entities are the vertices and the Links are edges.

The task is to write a program that clones a given entity and all its related entities in the entity graph.

## SPECIFICATIONS:

The program should take in as an input a JSON file representing the entities and the links in the system and the id of the entity that needs to be cloned. These inputs should be taken in on the command line as follows:

```
<program_name> <inputfile> <entityid>
```

The JSON file contains the following information:

 - entities : A list of all the entities in the system. You can assume that the ids are unique integers.
 - links : a list of all the links in the system

The format of the JSON file is as follows:

```
{
  "entities": [
    {
      "entity_id": 3,
      "name": "EntityA"
    },
    {
      "entity_id": 5,
      "name": "EntityB"
    },
    {
      "entity_id": 7,
      "name": "EntityC",
      "description": "More details about entity C"
    },
    {
      "entity_id": 11,
      "name": "EntityD"
    }
  ],
  "links": [
    {
      "from": 3,
      "to": 5
    },
    {
      "from": 3,
      "to": 7
    },
    {
      "from": 5,
      "to": 7
    },
    {
      "from": 7,
      "to": 11
    }
  ]
}
```

After reading the json file the program needs to do the following:

 - Find the entity with the entityid given as a parameter on the command line. This is referred to as the initial entity below.
 - Create a clone (copy) of the initial entity and assign it a new id. You can use any unique integer id that does not exist in the current list.
 - Clone all the related entities by traversing the links from the initial entity. This proces should continue till all the related entities and links have been cloned. Note there may be loops in entities, i.e. an entity may link back to one of its ancestors. This case needs to be handled.
 - For the initial entity, any entities that link to it must now also link to the clone of the initial entity.
 - All the new entities and links should be added back to the list of entities and links. 
 - When the cloning is completed, the program should output the entities and links to standard output as valid JSON in the same format as the input file.

## EXAMPLE:
For the input file shown above, and entity_id to clone as 5, the expected output is as follows: (Note: order of entities and links does not matter. The ids assigned to the cloned entities does not matter, as long as they are unique.)

Output:

```
{
  "entities": [
    {
      "entity_id": 3,
      "name": "EntityA"
    },
    {
      "entity_id": 5,
      "name": "EntityB"
    },
    {
      "entity_id": 7,
      "name": "EntityC",
      "description": "More details about entity C"
    },
    {
      "entity_id": 11,
      "name": "EntityD"
    },
    {
      "entity_id": 13,
      "name": "EntityB"
    },
    {
      "entity_id": 17,
      "name": "EntityC",
      "description": "More details about entity C"
    },
    {
      "entity_id": 19,
      "name": "EntityD"
    }
  ],
  "links": [
    {
      "from": 3,
      "to": 5
    },
    {
      "from": 3,
      "to": 7
    },
    {
      "from": 5,
      "to": 7
    },
    {
      "from": 7,
      "to": 11
    },
    {
      "from": 3,
      "to": 13
    },
    {
      "from": 13,
      "to": 17
    },
    {
      "from": 17,
      "to": 19
    }
  ]
}
```

## GUIDELINES:

1. You can use any of the following programming languages per your preference: C, C++, C#, Go, Java, Javascript, Python, Ruby, Typescript
2. The code must be functionally correct and handle all conditions and edge cases. Any submission which does not pass our functional tests will be rejected.
3. The program should be runnable on the command line. Please include a readme or a note documenting how to run your program on the command line.
4. The code should be readable and well structured. You can choose any appropriate internal representation for the Entities and Links.
5. The code should be performant as the size of the input file may be large.
6. The code should have good unit tests.
7. The code should be written as production quality code and be reasonably well documented.
8. We expect this to take 2‐3 hours of development effort. If you find yourself spending a lot more time than that please let us know.