using System;

namespace SimpleChatClient.Commands
{
    using System.Windows.Input;
    class SendChatKeyDownCommand :ICommand
    {
        private ViewModels.ChatWindowViewModel _ViewModel;
        public SendChatKeyDownCommand(ViewModels.ChatWindowViewModel viewmodel)
        {
            _ViewModel = viewmodel;
        }
        #region ICommand member
        public event EventHandler CanExecuteChanged
        {
            add { CommandManager.RequerySuggested += value; }
            remove { CommandManager.RequerySuggested -= value; }
        }


        public bool CanExecute(object parameter)
        {
            return true;
        }

        public void Execute(object parameter)
        {
            _ViewModel.Chat();
        }
        #endregion
    }
}
