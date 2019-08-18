namespace SimpleChatClient.Commands
{
    using System.Windows.Input;
    using System;

    using SimpleChatClient.ViewModels;

    internal class RefreshCommand : ICommand
    {
        /// <summary>
        /// Initailizes a new instance of the RoomRefreshCommand class;
        /// </summary>
        public RefreshCommand(RoomListWindowViewModel viewModel)
        {
            _ViewModel = viewModel;
        }
        private RoomListWindowViewModel _ViewModel;

        #region ICommand Member

        public event EventHandler CanExecuteChanged
        {
            add { CommandManager.RequerySuggested += value; }
            remove { CommandManager.RequerySuggested -= value; }
        }

        public bool CanExecute(object parameter)
        {
            return _ViewModel.CanListUpdate;
        }

        public void Execute(object parameter)
        {
            _ViewModel.DownloadRoomData();
        }
        #endregion
    }
}
