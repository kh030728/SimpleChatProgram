namespace SimpleChatClient.Commands
{
    using System;
    using System.Windows.Input;

    internal class ChatButton : ICommand
    {
        private ViewModels.ChatWindowViewModel _ViewModel;
        public ChatButton(ViewModels.ChatWindowViewModel viewModel)
        {
            _ViewModel = viewModel;
        }
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
    }
}
