package io.limkhashing.customwidgets.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import io.limkhashing.customwidgets.databinding.ViewSavingsItemBinding
import io.limkhashing.customwidgets.models.Savings

class ExampleAdapter(private val interaction: Interaction? = null) : RecyclerView.Adapter<ExampleAdapter.SavingsViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Savings>() {
        override fun areItemsTheSame(oldItem: Savings, newItem: Savings): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Savings, newItem: Savings): Boolean {
            return false
        }
    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingsViewHolder {
        return SavingsViewHolder(ViewSavingsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), interaction)
    }

    override fun onBindViewHolder(holder: SavingsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Savings>) {
        differ.submitList(list)
    }

    inner class SavingsViewHolder(private val binding: ViewSavingsItemBinding, private val interaction: Interaction?) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Savings) = with (binding) {
            binding.item = item
            executePendingBindings()
//            Glide.with(binding.root.context)
//                .load(R.drawable.sample)
//                .into(binding.ivOutletLogo)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Savings, itemView: View)
    }
}
