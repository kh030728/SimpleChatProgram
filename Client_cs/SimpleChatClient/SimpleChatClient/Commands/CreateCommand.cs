namespace SimpleChatClient.Commands
{
    using System;
    using System.Windows;
    using System.Windows.Input;

    using SimpleChatClient.ViewModels;

    internal class CreateCommand : ICommand
    {
        /// <summary>
        /// Initializes a new instance of the CreateCommand class;
        /// </summary>
        public CreateCommand(RoomListWindowViewModel viewModel)
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
            CreateRoomDialog createRoomDialog = new CreateRoomDialog();
            if (createRoomDialog.ShowDialog() == true)
            {
                if (CreateRoomDialog.ReturnValue.Contains("_"))
                {
                    MessageBox.Show("'_' 문자를 포함할 수 없습니다.", "오류", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }
                Console.WriteLine("Room Name : {0}", CreateRoomDialog.ReturnValue);
                _ViewModel.CreateRoom(CreateRoomDialog.ReturnValue);
            }
        }

        #endregion
    }
}
