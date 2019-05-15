class LikeButton extends React.Component {
  constructor(props) {
    super(props);
    this.state = { liked: false };
  }

  render() {
    if (this.state.liked) {
      return [
          e( 'span', { className: "qa-liked-message" }, 'You liked this.' ),
          ' Awesome !!!'
      ];
    }

    return e(
      'button',
      {
        className: "qa-like-button",
        onClick: () => this.setState({ liked: true })
      },
      'Like'
    );
  }
}

ReactDOM.render(e(LikeButton), document.querySelector('#like_button_container'));