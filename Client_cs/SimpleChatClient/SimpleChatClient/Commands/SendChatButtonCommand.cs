namespace SimpleChatClient.Commands
{
    using System;
    using System.Windows.Input;

    class SendChatButtonCommand : ICommand
    {
        private ViewModels.ChatWindowViewModel _ViewModel;
        public SendChatButtonCommand(ViewModels.ChatWindowViewModel viewModel)
        {
            _ViewModel = viewModel;
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
