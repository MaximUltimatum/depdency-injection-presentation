import any.necessesary.classes;

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
