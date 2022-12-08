import any.necessesary.classes;

class BetterRepairTicketsMySQLRepository
{
    private final RepairTicketRepoInterface repo;
    public BetterRepairTicketsMySQLRepository(DatabaseStore store)
    {
        repo = store.getRepairRepoThreadsafeImmutableSingleton();
    }

    public void processReceivedTickets(List<RepairTicket> tickets,
       ReplaceTicketsRepoInterface replaceRepo,
       NormalTicketRepoInterface normalRepo,
       ... /*Other grouped dependencies that can be injected, ideally pre-constructed or with a helper method that constructs or grabs the thread-safe singleton */)
    {
        //...more setup stuff...
        tickets.stream().forEach(ticket -> validate(ticket));
        tickets.stream().forEach(ticket -> checkEachForRelationsAndUpdateAsNeeded(ticket, replaceRepo));
        tickets.stream().forEach(ticket -> checkEachForRelationsAndUpdateAsNeeded(ticket, normalRepo));
    }
}

/*
What happens if:
    - The table name changes?
    - The database type changes?
    - The behavior necessary to get a table changes?
    - The codebase changes database types, or needs to support multiple kinds of database?
From these questions, we can gather:
    - Dependencies are given, pre-built, to the method that needs them
    - Easily testable - mocks for the Repos can be passed in from any testing framework that supports Mocking
    - *quickly* testable - while mocked database frameworks for testing database-dependant code exist,
        they are extremely slow (~5m for tests) and should only be used for integration tests, as they remove the possibility of test-driven development
    - Dependency Injection is also a cornerstone of being able to do SOLID correctly. By ONLY implementing changes to conform to Dependency Injection, now:
        - method depends on an interface, giving us dependency-inversion conformance for free!
        - method now depends on a _high level_ interface (not MySQL specific). This helps us meet the Interface segregation principle
            and a separate interface can be created (i.e. "MySQLRepository") can be created as needed so classes don't depend on behavior they don't have to.
        - method now conforms (better) to the Open-Closed principle. We no longer need to edit it if the behavior of the other repositories change,
            instead we can simply extend those classes or make a new interface implementation
 */
