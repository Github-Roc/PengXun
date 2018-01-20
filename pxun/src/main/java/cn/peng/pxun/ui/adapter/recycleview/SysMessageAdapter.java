package cn.peng.pxun.ui.adapter.recycleview;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.peng.pxun.R;
import cn.peng.pxun.modle.bmob.SysMessage;
import cn.peng.pxun.presenter.activity.SysMessagePresenter;
import cn.peng.pxun.ui.activity.SysMessageActivity;
import cn.peng.pxun.utils.ToastUtil;

/**
 * Created by tofirst on 2017/9/28.
 */

public class SysMessageAdapter extends RecyclerView.Adapter<SysMessageAdapter.SysMessageViewHolder>{

    private SysMessageActivity activity;
    private SysMessagePresenter presenter;
    private List<SysMessage> data;

    public SysMessageAdapter(SysMessageActivity activity, SysMessagePresenter presenter){
        this.activity = activity;
        this.presenter = presenter;
    }

    public void setSysMessageList(List<SysMessage> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public SysMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // View view = View.inflate(activity, R.layout.item_sys_message, null);
        View view = LayoutInflater.from(activity).inflate(R.layout.item_sys_message, parent, false);
        return new SysMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SysMessageViewHolder holder, int position) {
        final SysMessage sysMsg = data.get(position);
        holder.tv_date.setText(sysMsg.getCreatedAt());
        holder.tv_message.setText(sysMsg.getMessage());

        holder.tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("0".equals(sysMsg.getMsgState())){
                    showAddContactDialog(sysMsg);
                }else{
                    ToastUtil.showToast(activity, "消息已处理");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        return data.size();
    }

    private void showAddContactDialog(final SysMessage sysMsg) {
        new AlertDialog.Builder(activity)
                .setTitle("好友申请")
                .setMessage(sysMsg.getMessage())
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.agreeAddContact(sysMsg);
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }

    class SysMessageViewHolder extends RecyclerView.ViewHolder{
        TextView tv_date;
        TextView tv_message;

        public SysMessageViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_message_date);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message_content);
        }
    }
}
