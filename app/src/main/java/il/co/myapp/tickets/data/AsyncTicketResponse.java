package il.co.myapp.tickets.data;

import java.util.List;

import il.co.myapp.tickets.model.Ticket;


public interface AsyncTicketResponse {
    void TicketsDataReceived(List<Ticket> tickets);
}
