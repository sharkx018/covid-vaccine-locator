package com.example.vaccinechecker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinechecker.data.model.locations.Session
import com.example.vaccinechecker.databinding.SessionLayoutBinding

class SessionAdaptor: ListAdapter<Session, SessionAdaptor.SessionRowViewHolder>(DiffItemCallBack()) {

    inner class SessionRowViewHolder(val binding: SessionLayoutBinding) : RecyclerView.ViewHolder(binding.root){

    }



    override fun onBindViewHolder(holder: SessionRowViewHolder, position: Int) {
        val session = getItem(position);

        holder.binding.availableCount.text = session.available_capacity.toString()
        holder.binding.minageLabel.text = "Min age: ${session.min_age_limit}"

        holder.binding.dateVal.text = session.date.toString()
        holder.binding.slot1.text = session.slots?.get(0) ?: ""
        holder.binding.slot2.text = session.slots?.get(1) ?: ""
        holder.binding.slot3.text = session.slots?.get(2) ?: ""
        holder.binding.slot4.text = session.slots?.get(3) ?: ""

        holder.binding.centerVal.text = "Center Name: ${session.block_name}";
        holder.binding.pincodeVal.text = "Pincode: ${session.pincode}";
        holder.binding.vaccineVal.text = "${session.vaccine}";

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionRowViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context);
        val binding = SessionLayoutBinding.inflate(layoutInflater, parent, false)
        return SessionRowViewHolder(binding)

    }


    class DiffItemCallBack: DiffUtil.ItemCallback<Session>(){
        override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem.session_id == newItem.session_id;
        }

        override fun areContentsTheSame(oldItem: Session, newItem: Session): Boolean {
            return oldItem == newItem;
        }

    }



}