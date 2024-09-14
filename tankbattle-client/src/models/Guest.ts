export interface IGuest {
  name: string;
}

class Guest implements IGuest {
  public name: string;

  constructor(name: string) {
    this.name = name;
  }
}

export default Guest;
