namespace SimpleChatClient.Commands
{
    using System;
    using System.Windows.Input;

    using SimpleChatClient.ViewModels;

    internal class JoinCommand : ICommand
    {
        public JoinCommand(RoomListWindowViewModel viewModel)
        {
            _ViewModel = viewModel;
        }

        private RoomListWindowViewModel _ViewModel;

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
            _ViewModel.JoinRoom();
        }
        #endregion
    }
}
