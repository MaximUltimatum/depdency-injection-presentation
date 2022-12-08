import any.necessesary.classes;

/*
Problems:
No dependency injection
    - we're manually assembling database table objects, which should be passed in, in the constructor
    - how do we test this? What are you going to do if you have an edge case that you're not sure works, that depends on if a certain field is set in a ReplaceTicket?
    - This class fails the Dependency-Inversion principle, the Interface segregation principle, the Liskov Substitution principle, _and_ the Open-Closed principle
What happens if:
    - The table name changes?
    - The database type changes?
    - The behavior necessary to get a table changes?
    - The codebase changes database types, or needs to support multiple kinds of database?
When all we *actually* care about is setting related fields - we can use instance methods on things passed in to do that
 */
class BadRepairTicketsMySQLRepository
{
    private final Repo repo;
    public BadRepairTicketsMySQLRepository()
    {
        repo = DatabaseAccess.getTable("RepairTickets");
    }

    public void processReceivedTickets(List<RepairTicket> tickets)
    {
        //...more setup stuff...
        tickets.stream().forEach(ticket -> validate(ticket));
        ReplaceTicketsMySQLRepository replaceRepo = DatabaseAccess.getTable("ReplaceTickets");
        NormalOperationTicketsMySQLRepository normalRepo = DatabaseAccess.getTable("NormalTickets");
        //... more manual creation of things we need...
        tickets.stream().forEach(ticket -> checkEachForRelationsAndUpdateAsNeeded(ticket, replaceRepo));
        tickets.stream().forEach(ticket -> checkEachForRelationsAndUpdateAsNeeded(ticket, normalRepo));
    }


}
