namespace SimpleChatClient.ViewModels
{
    using System.Windows.Input;

    internal class LogOnWindowViewModel
    {
        public LogOnWindowViewModel()
        {
            ConnectCommand = new SimpleChatClient.Commands.ConnectCommand(this);
        }

        public ICommand ConnectCommand { get; private set; }
    }

}
