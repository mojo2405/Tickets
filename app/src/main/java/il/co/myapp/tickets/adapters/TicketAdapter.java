package il.co.myapp.tickets.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.activities.TicketDetailsActivity;
import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.model.Ticket;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {
    private Context _context;
    private List<Ticket> _ticketList;

    public TicketAdapter(Context context, List<Ticket> ticketsList) {

        _context = context;
        _ticketList = ticketsList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_row, parent, false);

        //view.setOnClickListener(ViewHolder.onClick(view));

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ticket ticket = _ticketList.get(position);


        holder.title.setText(ticket.getTicketTitle());
        holder.description.setText(ticket.getDescription());
    }

    @Override
    public int getItemCount() {
        return _ticketList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.dashboardTicketHeaderId);
            description = (TextView) itemView.findViewById(R.id.dashboardTicketDescriptionId);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            Ticket ticket = _ticketList.get(position);
            AppController.getInstance().set_viewableTicket(ticket);
            Intent intent = new Intent(_context, TicketDetailsActivity.class);
            _context.startActivity(intent);
        }
    }


}