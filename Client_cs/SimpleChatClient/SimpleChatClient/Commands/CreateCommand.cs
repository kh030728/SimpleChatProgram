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

        public RoomListWindowViewModel _ViewModel;

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
            Views.CreateRoomDialog createRoomDialog = new Views.CreateRoomDialog();
            if (createRoomDialog.ShowDialog() == true)
            {
                if (Views.CreateRoomDialog.ReturnValue.Contains("%$%"))
                {
                    MessageBox.Show("\"%$%\" 문자열을 포함할 수 없습니다.", "오류", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }
                Console.WriteLine("Room Name : {0}", Views.CreateRoomDialog.ReturnValue);
                _ViewModel.CreateRoom(Views.CreateRoomDialog.ReturnValue);
            }
        }

        #endregion
    }
}
