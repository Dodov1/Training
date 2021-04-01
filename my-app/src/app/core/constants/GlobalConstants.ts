export class GlobalConstants {
  public static apiURL: string = "https://localhost:8080/";

  public static currentUserId: number = Number(localStorage.getItem("userId"));

  public static currentTrainerId: number = Number(localStorage.getItem("trainerId"));

  public static siteTitle: string = "This is example of ItSolutionStuff.com";
}
